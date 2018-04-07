package com.fanw.fanwsocialapp.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.fanw.fanwsocialapp.R;
import com.fanw.fanwsocialapp.adapter.MessageAdapter;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private RecyclerView message_rv;
    private SwipeRefreshLayout message_swipe;
    private EditText message_content;
    private ImageView message_send;
    private String current_user_name;
    private String current_user_pwd;
    private String toSb;
    private  XMPPTCPConnection connection;
    private List<String> messageList = new ArrayList<>();
    private MessageAdapter messageAdapter;
    private Context context;
    private Handler mHandler= new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what){
                case 1:
                    Toast.makeText(getApplicationContext(),msg.obj+"",Toast.LENGTH_SHORT).show();
                    messageList.add(msg.obj+"");
                    messageAdapter.notifyDataSetChanged();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        connection=getConnection();
        initData();
        initView();
        context = getApplicationContext();
        messageAdapter = new MessageAdapter(messageList,context);
        message_rv.setHasFixedSize(true);
        message_rv.setLayoutManager(new LinearLayoutManager(context));
        message_rv.setAdapter(messageAdapter);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        connection.disconnect();
    }

    private void initData(){
        toSb = getIntent().getStringExtra("toSb");
        final File current_user= new File("/data/data/"+getPackageName()+"/shared_prefs","current_user.xml");
        if (current_user.exists()){
            SharedPreferences pre = getSharedPreferences("current_user", Context.MODE_PRIVATE);
            current_user_name = pre.getString("user_name","");
            current_user_pwd = pre.getString("user_pwd","");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        connection.connect();
                        connection.login(current_user_name, current_user_pwd);
                        Presence presence = new Presence(Presence.Type.available);
                        presence.setStatus("我是在线状态");
                        connection.sendStanza(presence);
                        ChatManager chatmanager = ChatManager.getInstanceFor(connection);
                        chatmanager.addChatListener(new ChatManagerListener() {
                            @Override
                            public void chatCreated(Chat chat, boolean createdLocally) {
                                chat.addMessageListener(new ChatMessageListener() {
                                    @Override
                                    public void processMessage(Chat chat, Message message) {
                                        String content=message.getBody();
                                        if (content!=null){
                                            Log.e("TAG", "from:" + message.getFrom() + " to:" + message.getTo() + " message:" + message.getBody());
                                            android.os.Message message1= android.os.Message.obtain();
                                            message1.what=1;
//                                            message1.obj="收到消息：" + message.getBody()+" 来自:"+message.getFrom();
                                            message1.obj = message.getFrom().substring(0,message.getFrom().indexOf("@"))+":"+message.getBody();
                                            mHandler.sendMessage(message1);
                                        }

                                    }
                                });
                            }
                        });
                    } catch (SmackException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (XMPPException e) {
                        e.printStackTrace();
                    }

                }
            }).start();
        }
    }

    private void initView(){
        mToolbar = findViewById(R.id.message_toolbar);
        message_rv = findViewById(R.id.message_rv);
        message_swipe = findViewById(R.id.message_swipe);
        message_content = findViewById(R.id.message_content);
        message_send = findViewById(R.id.message_send);
        message_send.setOnClickListener(this);
    }

    private XMPPTCPConnection getConnection(){
        String server="192.168.56.1";
        int port=5222;
        XMPPTCPConnectionConfiguration.Builder builder = XMPPTCPConnectionConfiguration.builder();
        builder.setServiceName(server);
        builder.setHost(server);
        builder.setPort(port);
        builder.setCompressionEnabled(false);
        builder.setDebuggerEnabled(true);
        builder.setSendPresence(true);
        builder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        XMPPTCPConnection connection = new XMPPTCPConnection(builder.build());
        return connection;
    }

    @Override
    public void onClick(View v) {
        final String c = message_content.getText().toString();
        if (TextUtils.isEmpty(toSb)||TextUtils.isEmpty(c)) {
            Toast.makeText(getApplicationContext(), "接收方或内容", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            ChatManager chatmanager = ChatManager.getInstanceFor(connection);
            Chat mChat = chatmanager.createChat(toSb+"@127.0.0.1");
            mChat.sendMessage(c);
            messageList.add("我："+c);
            messageAdapter.notifyDataSetChanged();
        }
        catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }
}
