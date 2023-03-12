package com.cashew.budgetservice.DTO;

import com.cashew.budgetservice.DAO.Entities.Party;
import lombok.Data;
import lombok.Value;

import java.util.List;

public enum PartiesDTO {;
    private interface PartyId {Long getPartyId();}
    private interface Name {String getName();}
    private interface OwnerId {Long getOwnerId();}
    private interface Users {List<String> getUsers();}
    private interface Parties {List<Party> getParties();}
    private interface FullInfo {Party getParty();}
    private interface Username {String getUsername();}

    public enum Request{;
        @Data
        public static class Create implements Name, OwnerId{
            String name;
            Long ownerId;
        }

        @Data
        public static class GetPartiesOf implements Username{
            String username;
        }

        @Data
        public static class AddUserToParty implements PartyId,Username{
            Long PartyId;
            String username;
        }

        @Data
        public static class RemoveUserFromParty implements PartyId,Username{
            Long PartyId;
            String username;
        }

    }

    public enum Response{;
        @Value
        public static class Created extends DTO implements PartyId {
            Long PartyId;
        }

        @Value
        public static class UsersList extends DTO implements Users{
            List<String> users;
        }

        @Value
        public static class PartiesList extends DTO implements Parties{
            List<Party> parties;
        }

        @Value
        public static class FullInfo extends DTO implements PartiesDTO.FullInfo {
            Party party;
        }

        @Value
        public static class Success extends DTO {
            Boolean success;
        }
    }
}
