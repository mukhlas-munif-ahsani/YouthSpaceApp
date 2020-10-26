package com.munifahsan.youthspaceapp.Chat.pres;

import com.munifahsan.youthspaceapp.Chat.ChatEvent;
import com.munifahsan.youthspaceapp.Chat.repo.ChatRoomRepo;
import com.munifahsan.youthspaceapp.Chat.repo.ChatRoomRepoInt;
import com.munifahsan.youthspaceapp.Chat.view.ChatRoomViewInt;
import com.munifahsan.youthspaceapp.EventBus.EventBus;
import com.munifahsan.youthspaceapp.EventBus.GreenRobotEventBus;

import org.greenrobot.eventbus.Subscribe;

import static com.munifahsan.youthspaceapp.Chat.ChatEvent.onError;
import static com.munifahsan.youthspaceapp.Chat.ChatEvent.onSuccess;

public class ChatRoomPres implements ChatRoomPresInt {
    private ChatRoomViewInt mChatRoomViewInt;
    private ChatRoomRepoInt mChatRoomRepoInt;
    private EventBus mEventBus;

    public ChatRoomPres(ChatRoomViewInt mChatRoomViewInt) {
        this.mChatRoomViewInt = mChatRoomViewInt;
        mChatRoomRepoInt = new ChatRoomRepo();
        mEventBus = GreenRobotEventBus.getInstance();
    }

    @Subscribe
    public void onEventMainThread(ChatEvent event) {
        switch (event.getEventType()) {
            case onSuccess:
                onSuccess(event.getImage(), event.getNama());
                break;
            case onError:
                onError();
                break;
        }
    }

    public void onSuccess(String imageUrl, String nama){
        mChatRoomViewInt.setProfileImage(imageUrl);
        mChatRoomViewInt.setNamaChatRoom(nama);
    }

    public void onError(){

    }

    @Override
    public void sendMessage(String senderId, String message){
        if (!message.isEmpty()){

        }
    }

    @Override
    public void getData(String chatRoomId){
        mChatRoomRepoInt.getData(chatRoomId);
    }

    @Override
    public void onCreate(){
        mEventBus.register(this);
    }

    @Override
    public void onDestroy(){
        mEventBus.unregister(this);
        mChatRoomViewInt = null;
    }
}
