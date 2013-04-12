package models.question;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.data.Language;
import models.user.User;

/**
 * The base class where questions for the competitions are stored in.
 * @author Ruben Taelman
 *
 */
public abstract class Question {

    /** These fields are generated on reading the XML **/
    protected String ID;
    protected Server server;
    protected QuestionType type;
    public List<Language> languages;
    protected Map<Language, String> titles;
    protected Map<Language, String> indexes;
    protected Map<Language, String> feedbacks;

    /** These fields can be altered afterwards **/
    public boolean official;
    public boolean active;
    public User author;

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
        return active;
    }

    /**
     * Sets the active value
     * @param active is this question active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Returns the server on which this question is located
     * @return the server location of this question
     */
    public Server getServer() {
        return server;
    }

    /**
     * Sets the server on which this question is located
     * @param server the server location of this question
     */
    public void setServer(Server server) {
        this.server = server;
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
    public String getID() {
        return ID;
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

}