package com.hotel.booking;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FeedbacksModel extends DatabaseModel {

	public FeedbacksModel() throws SQLException {
		super("feedbacks", "id");
	}

	public ArrayList<Feedback> getHotelFeedbacks(int id) throws SQLException {
		
		PreparedStatement stmt = this.connection.prepareStatement("SELECT * FROM feedbacks WHERE hotel = ?");
		stmt.setInt(1, id);
		ResultSet feedbacksSet = stmt.executeQuery();
		ArrayList<Feedback> feedbacks = new ArrayList<>();
		
		while(feedbacksSet.next()) {
			Feedback feedback = new Feedback();
			feedback.setRating(feedbacksSet.getInt("rating"));
			feedback.setComment(feedbacksSet.getString("comment"));
			feedbacks.add(feedback);
		}
		
		return feedbacks;

	}
        
        public void createFeedback(int id, int rating, String comment, String user) throws SQLException {
            
            PreparedStatement stmt = this.connection.prepareStatement("INSERT INTO feedbacks VALUES(null, ?, ?, ?, ?)");
            stmt.setInt(1, id);
            stmt.setInt(2, rating);
            stmt.setString(3, comment);       
            stmt.setString(4, user);
            stmt.execute();
            
        }
}
