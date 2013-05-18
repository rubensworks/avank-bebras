package models.question;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;
import it.sauronsoftware.ftp4j.FTPListParseException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.EMessages;
import models.data.Language;
import models.dbentities.QuestionModel;
import models.management.ModelState;
import models.user.AuthenticationManager;
import play.mvc.Call;
import controllers.EController;
import controllers.question.QuestionManager;
import controllers.question.routes;

/**
 * The base class where questions for the competitions are stored in.
 * @author Ruben Taelman
 *
 */
public abstract class Question {

    /** These fields are generated on reading the XML **/
    private QuestionModel model;
    protected QuestionType type;
    protected List<Language> languages;
    protected Map<Language, String> titles;
    protected Map<Language, String> indexes;
    protected Map<Language, String> feedbacks;

    /** These fields can be altered afterwards **/
    public boolean official;

    /**
     * Don't just call this, Question need to be generated by calling the static getFromXml(String xml)
     */
    protected Question() {
        this.languages = new ArrayList<Language>();
        this.titles = new HashMap<Language, String>();
        this.indexes = new HashMap<Language, String>();
        this.feedbacks = new HashMap<Language, String>();
    }

    /**
     * Set a model for the question data
     * @param model model with db data for the question
     */
    protected void setModel(QuestionModel model) {
        this.model = model;
    }

    /**
     * Retrieve a question by id
     * @param id the id of a question
     * @return question
     * @throws QuestionBuilderException if something went wrong
     */
    public static Question fetch(String id) throws QuestionBuilderException {
        // We'll first poll the questioncache before remaking the question
        Question question = QuestionCache.getQuestion(id);

        if(question == null) {
            // Check if the id is valid
            QuestionModel model = null;
            try {
                model = new QuestionManager(ModelState.DELETE).getFinder().byId(id);
            } catch (Exception e) {
                throw new QuestionBuilderException(EMessages.get("question.factory.error.invalidID"));
            }

            question = QuestionIO.getFromXml(routes.QuestionController.showQuestionFile(id, QuestionPack.QUESTIONXMLFILE).absoluteURL(EController.request()));
            question.setModel(model);

            // Add this to the cache
            QuestionCache.putQuestion(id, question);
        }
        return question;
    }

    /**
     * Is the question official
     * @return is official
     */
    public boolean isOfficial() {
        return official;
    }

    /**
     * Sets the official value
     * @param official is this question official
     */
    public void setOfficial(boolean official) {
        this.official = official;
    }

    /**
     * Is the question active
     * @return is active
     */
    public boolean isActive() {
        return model.active;
    }

    /**
     * Sets the active value
     * @param active is this question active
     */
    public void setActive(boolean active) {
        this.model.active = active;
    }

    /**
     * Returns the server on which this question is located
     * @return the server location of this question
     */
    public Server getServer() {
        return model.server;
    }

    /**
     * Sets the server on which this question is located
     * @param server the server location of this question
     */
    public void setServer(Server server) {
        this.model.server = server;
    }

    /**
     * Get the question type
     * @return the type of the question
     */
    public QuestionType getType() {
        return type;
    }

    /**
     * Add a possible language to the question
     * @param language a language
     */
    public void addLanguage(Language language) {
        this.languages.add(language);
    }

    /**
     * Returns a list of all available languages in this question
     * @return list of languages
     */
    public List<Language> getLanguages() {
        return languages;
    }

    /**
     * Returns a list of all languages that are not available in this question
     * @return list of languages
     */
    public List<Language> getNonLanguages() {
        List<Language> all = new ArrayList<Language>();
        for (Language language : Language.listLanguages()) {
            if(!languages.contains(language))
                all.add(language);
        }
        return all;
    }

    /**
     * Returns the unique ID of this question on a certain server
     * @return the ID of a question
     */
    public int getID() {
        return model.id;
    }

    /**
     * Sets the id of this question
     * @param id the id this question should have
     */
    public void setID(int id) {
        this.model.id = id;
    }

    /**
     * Returns the title in a certain Language
     * @param language chosen language
     * @return the title of this question in the given language
     */
    public String getTitle(Language language) {
        return this.titles.get(language);
    }

    /**
     * Sets the title for a given language
     * @param title the title for the question
     * @param language chosen language
     */
    public void setTitle(String title, Language language) {
        this.titles.put(language, title);
    }

    /**
     * Sets the index file name for a given language
     * @param title the index file name for the question
     * @param language chosen language
     */
    public void setIndex(String title, Language language) {
        this.indexes.put(language, title);
    }

    /**
     * Gets the index file name for a given language
     * @param language chosen language
     * @return the index file name for the question
     */
    public String getIndex(Language language) {
        return indexes.get(language);
    }

    /**
     * Gets the index file source for a given language
     * @param language chosen language
     * @return a link to the index file
     */
    public Call getIndexLink(Language language) {
        return routes.QuestionController.showQuestionFile(
                Integer.toString(this.getID()), this.getIndex(language)
               );
    }

    /**
     * Sets the feedback file name for a given language
     * @param title the feedback file name for the question
     * @param language chosen language
     */
    public void setFeedback(String title, Language language) {
        this.feedbacks.put(language, title);
    }

    /**
     * Gets the feedback file name for a given language
     * @param language chosen language
     * @return the feedback file name for the question
     */
    public String getFeedback(Language language) {
        return feedbacks.get(language);
    }

    /**
     * Gets the feedback file source for a given language
     * @param language chosen language
     * @return a link to the feedback file
     */
    public Call getFeedbackLink(Language language) {
        return routes.QuestionController.showQuestionFile(
                Integer.toString(this.getID()), this.getFeedback(language)
               );
    }

    /**
     * Generate an answer object for this question based on a certain input
     * @param input input for the answer
     * @param language language in which the answer is answered
     * @return the answer object for this question answer
     * @throws AnswerGeneratorException
     */
    public abstract Answer getAnswerByInput(String input, Language language) throws AnswerGeneratorException;

    /**
     * The data model of this question
     * @return the data model
     */
    public QuestionModel getData() {
        return this.model;
    }

    /**
     * @return official id
     */
    public String getOfficialID() {
        return this.model.officialid;
    }

    /**
     * Export this question to a zip archive
     * @return the file in an archive
     * @throws QuestionBuilderException
     * @throws FTPListParseException
     * @throws FTPAbortedException
     * @throws FTPDataTransferException
     * @throws FTPException
     * @throws FTPIllegalReplyException
     * @throws IOException
     * @throws IllegalStateException
     */
    public File export() throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException, FTPDataTransferException, FTPAbortedException, FTPListParseException, QuestionBuilderException {
        return this.getServer().downloadFile(Integer.toString(this.getID()), AuthenticationManager.getInstance().getUser().getID());
    }
}
