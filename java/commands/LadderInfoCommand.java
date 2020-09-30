package commands;

import coolGroup.LadderBot.Ref;
import coolGroup.LadderBot.UserConvertID;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import sqlClasses.LadderCheck;
import sqlClasses.Points;

public class LadderInfoCommand {

	private MessageChannel objMsgCh;
	private Message objMsg;
	private LadderCheck check;
	private Points points;

	// The authors user id converted to a string of user id numbers
	private String author;
	private UserConvertID authorUser;
	// The user ID as an string id number that was mentioned by the author of the
	// command
	private String user;
	private UserConvertID mentionedUser;

	// the User ID of the User that challenged the author of the message
/*	private LadderCheck grabChall;
	private String authorChallUser;*/

	public LadderInfoCommand(MessageReceivedEvent evt) {
		super();
		this.objMsgCh = evt.getChannel();
		this.objMsg = evt.getMessage();
		this.check = new LadderCheck();
		this.points = new Points();
		// The authors user id converted to a string of user id numbers
		this.author = objMsg.getAuthor().toString();
		this.authorUser = new UserConvertID(author);
		// The user ID as an string id number that was mentioned by the author of the
		// command
		this.user = objMsg.getMentions().toString();
		this.mentionedUser = new UserConvertID(user);

		// the User ID of the User that challenged the author of the message
/*		this.grabChall = new LadderCheck();
		this.authorChallUser = grabChall.grabChallenger(authorUser.conversion());*/
	}

	/**
	 * grabs the ladder rating for the author of the message
	 */
	public void myPoints() {
		// prints out the authors ladder points
		if (objMsg.getContentRaw().equalsIgnoreCase(Ref.prefix + "my rating")) {
			if (check.checkIfInLadder(authorUser.conversion())) {
				objMsgCh.sendMessage(" Your ladder Rating: " + points.getPoints(authorUser.conversion())).queue();
			} else {
				objMsgCh.sendMessage("You are not registered in the ladder ").queue();
			}
		}
	}

	public void top10() {
		// prints of the top ten ladder users
		if (objMsg.getContentRaw().equalsIgnoreCase(Ref.prefix + "Top 10")) {
			objMsgCh.sendMessage(points.ladderTop10()).queue();
		}
	}

	public void myLadder() {
		// getting a list of 10 ladder values with the authors rank being in the middle
		if (objMsg.getContentRaw().equalsIgnoreCase(Ref.prefix + "leaderboard")) {
			if (check.checkIfInLadder(authorUser.conversion())) {
				objMsgCh.sendMessage(points.myLadderRank(authorUser.conversion())).queue();
			} else {
				objMsgCh.sendMessage("You are not registered in the ladder ").queue();
			}
		}
	}

	public void stats() {
		// tells the stats of the mentioned user
		if (objMsg.getContentRaw().equalsIgnoreCase(Ref.prefix + "stats " + "<@" + mentionedUser.conversion() + ">")||
				objMsg.getContentRaw().equalsIgnoreCase(Ref.prefix + "stats " + "<@!" + mentionedUser.conversion() + ">")) {
			if (check.checkIfInLadder(mentionedUser.conversion())) {
				objMsgCh.sendMessage(check.grabStats(mentionedUser.conversion())).queue();
			} else {
				objMsgCh.sendMessage("That user is not registered in the ladder ").queue();
			}
		}
	}
	
}