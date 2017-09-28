package com.neeru.client.models;


/**
 * Created by brajendra on 13/07/17.
 * <p>
 * <p>
 * firstName: DataTypes.STRING,
 * lastName: DataTypes.STRING,
 * password: {
 * type: DataTypes.STRING,
 * validate: { min: 6 },
 * allowNull: false
 * },
 * avatar: DataTypes.STRING,
 * contact: {
 * type: DataTypes.STRING,
 * unique: true,
 * allowNull: false,
 * },
 * description: DataTypes.STRING,
 * email: DataTypes.STRING,
 */


public class User {

    public Integer id;
    public String firstName;
    public String lastName;
    public String avatar;
    public String contact;
    public String email;
    public String description;
    public String roles;
    public String accessToken;
}
