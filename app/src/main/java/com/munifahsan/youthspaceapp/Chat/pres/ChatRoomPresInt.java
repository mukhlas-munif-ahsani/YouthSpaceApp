package com.munifahsan.youthspaceapp.Chat.pres;

public interface ChatRoomPresInt {
    void sendMessage(String senderId, String message);

    void getData(String chatRoomId);

    void onCreate();

    void onDestroy();
}
