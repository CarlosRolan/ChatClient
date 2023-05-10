package controller.chats;

import controller.ClientOld;

public class Member {

    private enum Permissions {
        ADMIN(1,1,1),
        COOPER(1,1,0),
        REGULAR(1,0,0),
        ONLY_READ(0,0,0);
    
        Permissions(int canEdit, int canAdd, int canDelete) {
            
        }
    }

    private String name;
    private Permissions permType;

    public static Member newMember(ClientOld cc, boolean isAdmin) {
        String memberName = cc.getNick();

        if (isAdmin) {
            return new Member(memberName, Permissions.ADMIN);
        }
        return new Member(memberName, Permissions.REGULAR);
    }

    private Member(String name, final Permissions rights) {
        this.name = name;
        permType = rights;
    }

    public String getMemberInfo() {
        return name + " RIGHTS: " + permType;
    }

    public void makeAdmin() {
        permType = Permissions.ADMIN;
    }

    public void makeCooperator() {
        permType = Permissions.COOPER;
    }

    public void makeRegular() {
        permType = Permissions.REGULAR;
    }
  
}
