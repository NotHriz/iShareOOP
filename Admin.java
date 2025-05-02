import java.util.List;

public class Admin extends User {

    public Admin(String username, String email, String password) {
        super(username, email, password);
    }

    // Delete a user by username
    public boolean deleteUser(String username, UserManager userManager) {
        User userToRemove = userManager.findUserByUsername(username);
        if (userToRemove != null && !(userToRemove instanceof Admin)) {
            List<User> internalList = userManager.getAllUsers(); 
            internalList.remove(userToRemove);
            return true;
        }
        return false;
    }

    // Delete a question by ID
    public boolean deleteQuestion(int id, QuestionManager questionManager) {
        List<Question> questions = questionManager.getAllQuestions();
        for (Question q : questions) {
            if (q.getId() == id) {
                questions.remove(q);
                questionManager.saveToFile();
                return true;
            }
        }
        return false;
    }
}
