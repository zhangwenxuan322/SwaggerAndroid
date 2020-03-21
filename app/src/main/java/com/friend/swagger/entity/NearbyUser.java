package com.friend.swagger.entity;

/**
 * @Author ZhangWenXuan
 * @Date 2020-03-21 20:26
 **/
public class NearbyUser {
    private String nearbyId;
    private String nearbyPortrait;
    private String nearbyUserName;
    private String nearbyDistance;
    private String nearbyUserBio;

    public NearbyUser() {
    }

    public NearbyUser(String nearbyId, String nearbyPortrait, String nearbyUserName, String nearbyDistance, String nearbyUserBio) {
        this.nearbyId = nearbyId;
        this.nearbyPortrait = nearbyPortrait;
        this.nearbyUserName = nearbyUserName;
        this.nearbyDistance = nearbyDistance;
        this.nearbyUserBio = nearbyUserBio;
    }

    public String getNearbyId() {
        return nearbyId;
    }

    public void setNearbyId(String nearbyId) {
        this.nearbyId = nearbyId;
    }

    public String getNearbyPortrait() {
        return nearbyPortrait;
    }

    public void setNearbyPortrait(String nearbyPortrait) {
        this.nearbyPortrait = nearbyPortrait;
    }

    public String getNearbyUserName() {
        return nearbyUserName;
    }

    public void setNearbyUserName(String nearbyUserName) {
        this.nearbyUserName = nearbyUserName;
    }

    public String getNearbyDistance() {
        return nearbyDistance;
    }

    public void setNearbyDistance(String nearbyDistance) {
        this.nearbyDistance = nearbyDistance;
    }

    public String getNearbyUserBio() {
        return nearbyUserBio;
    }

    public void setNearbyUserBio(String nearbyUserBio) {
        this.nearbyUserBio = nearbyUserBio;
    }

    @Override
    public String toString() {
        return "NearbyUser{" +
                "nearbyId='" + nearbyId + '\'' +
                ", nearbyPortrait='" + nearbyPortrait + '\'' +
                ", nearbyUserName='" + nearbyUserName + '\'' +
                ", nearbyDistance='" + nearbyDistance + '\'' +
                ", nearbyUserBio='" + nearbyUserBio + '\'' +
                '}';
    }
}
