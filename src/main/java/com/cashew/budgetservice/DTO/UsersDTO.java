package com.cashew.budgetservice.DTO;

import lombok.Data;
import lombok.Value;

public enum UsersDTO {;
    private interface Id { Long getId(); }
    private interface Username { String getUsername(); }
    private interface Email { String getEmail(); }

    public enum Request{;
        @Data
        public static class Create implements Username, Email {
            String username;
            String email;
        }

        @Data
        public static class UpdateUser implements Id, Username, Email{
            Long id;
            String username;
            String email;
        }

        @Data
        public static class GetById implements Id{
            Long id;
        }

        @Data
        public static class GetByEmail implements Email{
            String email;
        }

        @Data
        public static class GetByUsername implements Username{
            String username;
        }

        @Data
        public static class Delete implements Id{
            Long id;
        }
    }

    public enum Response{;
        @Value
        public static class Created implements Id{
            Long id;
        }

        @Value
        public static class Updated implements Id, Username, Email{
            Long id;
            String username;
            String email;
        }

        @Value
        public static class Found implements Id, Username, Email{
            Long id;
            String username;
            String email;
        }

        @Value
        public static class Deleted {
            boolean success;
        }
    }
}
