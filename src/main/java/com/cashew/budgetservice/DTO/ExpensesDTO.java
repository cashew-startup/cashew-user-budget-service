package com.cashew.budgetservice.DTO;

import com.cashew.budgetservice.DAO.Entities.UserCheck;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public enum ExpensesDTO {;
    private interface Username { String getUsername(); }
    private interface Expenses { List<UserCheck> getExpenses();}

    public enum Request{;

        @Data
        @NoArgsConstructor
        public static class perLastDay implements Username {
            String username;
        }

        @Data
        @NoArgsConstructor
        public static class perLastWeek implements Username {
            String username;
        }

        @Data
        @NoArgsConstructor
        public static class perLastMonth implements Username {
            String username;
        }

        @Data
        @NoArgsConstructor
        public static class perLastYear implements Username {
            String username;
        }

        @Data
        @NoArgsConstructor
        public static class perCustomPeriod implements Username {
            String username;
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
            LocalDateTime from;
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
            LocalDateTime to;
        }
    }

    public enum Response{;
        @EqualsAndHashCode(callSuper = true)
        @Value
        public static class Expenses extends DTO implements ExpensesDTO.Expenses{
            List<UserCheck> expenses;
        }
    }
}

