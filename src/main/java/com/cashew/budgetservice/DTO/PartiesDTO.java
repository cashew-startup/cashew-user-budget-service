package com.cashew.budgetservice.DTO;

import com.cashew.budgetservice.DAO.Entities.Party;
import lombok.Data;
import lombok.Value;

import java.util.List;

public enum PartiesDTO {;
    private interface PartyId {Long getPartyId();}
    private interface Name {String getName();}
    private interface OwnerUsername {String getOwnerUsername();}
    private interface Users {List<String> getUsers();}
    private interface Parties {List<Party> getParties();}
    private interface FullInfo {Party getParty();}
    private interface Username {String getUsername();}

    public enum Request{;
        @Data
        public static class Create implements Name, OwnerUsername{
            String name;
            String ownerUsername;
        }

        @Data
        public static class GetPartiesOf implements Username{
            String username;
        }

        @Data
        public static class AddUserToParty implements PartyId,Username{
            Long partyId;
            String username;
        }

        @Data
        public static class RemoveUserFromParty implements PartyId,Username{
            Long partyId;
            String username;
        }

        @Data
        public static class GetFullInfo implements PartyId{
            Long partyId;
        }

        @Data
        public static class Delete implements PartyId{
            Long partyId;
        }

        @Data
        public static class ChangeName implements PartyId, Name{
            Long partyId;
            String name;
        }

    }

    public enum Response{;
        @Value
        public static class Created implements PartyId {
            Long partyId;
        }

        @Value
        public static class UsersList implements Users{
            List<String> users;
        }

        @Value
        public static class PartiesList implements Parties{
            List<Party> parties;
        }

        @Value
        public static class FullInfo implements PartiesDTO.FullInfo {
            Party party;
        }

        @Value
        public static class Success {
            Boolean success;
        }
    }
}
