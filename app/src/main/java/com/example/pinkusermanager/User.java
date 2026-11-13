package com.example.pinkusermanager;

public class User {
    private String Name, State;
    private int Age;
    private int StateSignal;
    private boolean archived;
    private String profileId;

    public User(String name, String state, int age, int stateSignal) {
        this.Name = name;
        this.State = state;
        this.Age = age;
        this.StateSignal = stateSignal;
        this.archived = false;
    }

    public String getName() { return Name; }
    public void setName(String name) { Name = name; }
    public String getState() { return State; }
    public void setState(String state) { State = state; }
    public int getAge() { return Age; }
    public void setAge(int age) { Age = age; }
    public int getStateSignal() { return StateSignal; }
    public void setStateSignal(int stateSignal) { StateSignal = stateSignal; }
    public boolean isArchived() { return archived; }
    public void setArchived(boolean archived) { this.archived = archived; }
    public String getProfileId() { return profileId; }
    public void setProfileId(String profileId) { this.profileId = profileId; }
}