package com.cashew.budgetservice.DTO;

import com.cashew.budgetservice.DAO.Entities.Receipt;
import com.cashew.budgetservice.DAO.Entities.UserCheck;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public enum ExpensesDTO {;
    private interface Username { String getUsername(); }
    private interface Expenses { List<Receipt> getExpenses();}
    private interface Token { String getToken(); }
    private interface Date { String getDate(); }
    private interface From { String getFrom(); }
    private interface To { String getTo(); }
    private interface FetchedReceipt { Receipt getFetchedReceipt(); }

    public enum Request{;

        @Data
        @AllArgsConstructor
        public static class Fetch implements Username, Token {
            String username;
            String token;
        }

        @Data
        public static class AddReceipt implements Username, Token {
            String username;
            String token;
        }

        @Data
        public static class perLastDay implements Username {
            String username;
        }

        @Data
        public static class perLastWeek implements Username {
            String username;
        }

        @Data
        public static class perLastMonth implements Username {
            String username;
        }

        @Data
        public static class perLastYear implements Username {
            String username;
        }

        @Data
        public static class perCustomPeriod implements Username {
            String username;
            String from;
            String to;
        }
    }

    public enum Response{;
        @Data
        public static class RequestedChecks implements Expenses{
            List<Receipt> expenses;

            public RequestedChecks setExpensesAsChecks(Iterable<UserCheck> checks) {
                expenses = new ArrayList<>();
                checks.forEach((check) -> expenses.add(check.getReceipt()));
                return this;
            }
        }

        @Value
        public static class FetchedReceiptInfo implements Username, Date, FetchedReceipt{
            String username;
            String date;
            Receipt fetchedReceipt;
        }

        @Value
        public static class Success {
            boolean success;
        }
    }
}

