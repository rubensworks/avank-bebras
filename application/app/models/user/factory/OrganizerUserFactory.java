package models.user.factory;

import models.dbentities.UserModel;
import models.user.Organizer;
import models.user.User;

/**
 * Make organizers
 * @author Ruben Taelman
 *
 */
public class OrganizerUserFactory implements UserFactory{

    @Override
    public User create(UserModel userModel) {
        return new Organizer(userModel);
    }

}
