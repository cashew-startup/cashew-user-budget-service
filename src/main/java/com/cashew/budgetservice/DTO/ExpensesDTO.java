package com.cashew.budgetservice.DTO;

import com.cashew.budgetservice.DAO.Entities.Receipt;
import com.cashew.budgetservice.DAO.Entities.UserCheck;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public enum ExpensesDTO {;
    private interface Username { String getUsername(); }
    private interface Expenses { List<Receipt> getExpenses();}

    public enum Request{;

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
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
            LocalDateTime from;
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
            LocalDateTime to;
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
    }
}

