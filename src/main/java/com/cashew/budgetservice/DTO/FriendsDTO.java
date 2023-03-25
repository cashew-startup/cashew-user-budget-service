package com.cashew.budgetservice.DTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;

public enum FriendsDTO {;
    private interface Username {String getUsername();}
    private interface Sender {String getSender();}
    private interface Receiver {String getReceiver();}
    private interface Deleter {String getDeleter();}
    private interface Deleted {String getDeleted();}
    private interface Friends {List<String> getFriends();}
    private interface FriendRequests {List<String> getFriendRequests();}


    public enum Request{;
        @Data
        public static class Get implements Username{
            String username;
        }

        @Data
        public static class FriendRequest implements Sender, Receiver{
            String sender;
            String receiver;
        }

        @Data
        public static class RemoveFriend implements Deleter, Deleted{
            String deleter;
            String deleted;
        }
    }

    public enum Response{;
        @EqualsAndHashCode(callSuper = false)
        @Value
        public static class GetFriends implements Friends{
            List<String> friends;
        }

        @Value
        public static class GetFriendRequests implements FriendRequests{
            List<String> friendRequests;
        }

        @Value
        public static class Success{
            boolean success;
        }
    }

}
