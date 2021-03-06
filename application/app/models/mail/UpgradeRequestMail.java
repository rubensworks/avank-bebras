/**
 *
 */
package models.mail;

import java.io.File;

import play.Play;

import models.EMessages;
import models.user.AuthenticationManager;
import models.user.User;

/**
 * @author Jens N. Rammant
 * Class for Email sent by Upgrade Request
 */
public class UpgradeRequestMail extends EMail {

    /**
     * Constructor
     * @param cardImage Image to be sent as Attachment
     * @param fileName Name of the Image
     */
    public UpgradeRequestMail(File cardImage,String fileName){
        super();
        this.addAttachment(cardImage,fileName);

        this.setSubject(EMessages.get("contact.upgraderequest.subject"));
        User current = AuthenticationManager.getInstance().getUser();
        if(current!=null&&current.data!=null&&current.data.id!=null&&current.data.email!=null){
            this.setMessage("id: "+current.data.id);
            this.addReplyTo(current.data.email);
        }
        this.addToAddress(Play.application().configuration().getString("email.upgrademail"));
    }

}
