package controller.chats;

public class Member {

    public enum Permissions {
        ADMIN(1,1,1),
        COOPER(1,1,0),
        REGULAR(1,0,0),
        ONLY_READ(0,0,0);
    
        Permissions(int canEdit, int canAdd, int canDelete) {
            
        }
    }

    private String name;
    private Permissions permType;

    public String getMemberName() {
        return name;
    }

    public static Member newMember(String nick, boolean isAdmin) {
    
        if (isAdmin) {
            return new Member(nick, Permissions.ADMIN);
        }
        return new Member(nick, Permissions.REGULAR);
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

    @Override
    public String toString() {
        return name + ":" + permType;
    }
  
}
