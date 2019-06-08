package tech.xlogix.threeSum.model;

public class User {
    private String Name, Email, Phone, UserDateOfBirth, Type, Password, UserCreatedOn, ProfilePic;

    public User() {
    }

    public User(String name, String email, String phone, String type, String password, String userCreatedOn, String profilePic) {
        Name = name;
        Email = email;
        Phone = phone;
        Type = type;
        Password = password;
        UserCreatedOn = userCreatedOn;
        ProfilePic = profilePic;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getUserCreatedOn() {
        return UserCreatedOn;
    }

    public void setUserCreatedOn(String userCreatedOn) {
        UserCreatedOn = userCreatedOn;
    }

    public String getProfilePic() {
        return ProfilePic;
    }

    public void setProfilePic(String profilePic) {
        ProfilePic = profilePic;
    }
}