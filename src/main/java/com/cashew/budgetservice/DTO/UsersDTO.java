package com.cashew.budgetservice.DTO;

import lombok.Data;
import lombok.Value;

public enum UsersDTO {;
    private interface Username { String getUsername(); }
    private interface NewUsername { String getNewUsername(); }
    private interface Email { String getEmail(); }
    private interface NewEmail { String getNewEmail(); }

    public enum Request{;
        @Data
        public static class Create implements Username, Email {
            String username;
            String email;
        }

        @Data
        public static class GetByUsername implements Username{
            String username;
        }


        @Data
        public static class GetByEmail implements Email{
            String email;
        }

        @Data
        public static class UpdateUsername implements NewUsername, Email{
            String newUsername;
            String email;
        }

        @Data
        public static class UpdateEmail implements Username, NewEmail{
            String username;
            String newEmail;
        }

        @Data
        public static class Delete implements Username{
            String username;
        }
    }

    public enum Response{;
        @Value
        public static class Created {
            boolean success;
        }

        @Value
        public static class Updated implements Username, Email{
            String username;
            String email;
        }

        @Value
        public static class Found implements Username, Email{
            String username;
            String email;
        }

        @Value
        public static class Deleted {
            boolean success;
        }
    }
}
