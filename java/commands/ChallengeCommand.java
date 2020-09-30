package commands;

import java.util.Timer;
import java.util.TimerTask;

import coolGroup.LadderBot.Ref;
import coolGroup.LadderBot.UserConvertID;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import sqlClasses.ChallengerUpdate;
import sqlClasses.LadderCheck;
import sqlClasses.Points;

public class ChallengeCommand {

	private User objUser;
	private MessageChannel objMsgCh;
	private Message objMsg;
	private LadderCheck check;
	private ChallengerUpdate update;
	private Points points;
	// The authors user id converted to a string of user id numbers
	private String author;
	private UserConvertID authorUser;
	// The user ID as an string id number that was mentioned by the author of the
	// command
	private String user;
	private UserConvertID mentionedUser;

	// the User ID of the User that challenged the author of the message
	private LadderCheck grabChall;
	private String authorChallUser;

	public ChallengeCommand(MessageReceivedEvent evt) {
		super();
		this.objUser = evt.getAuthor();
		this.objMsgCh = evt.getChannel();
		this.objMsg = evt.getMessage();
		evt.getGuild();
		this.check = new LadderCheck();
		this.update = new ChallengerUpdate();
		this.points = new Points();
		evt.getMember();
		// The authors user id converted to a string of user id numbers
		this.author = objMsg.getAuthor().toString();
		this.authorUser = new UserConvertID(author);
		// The user ID as an string id number that was mentioned by the author of the
		// command
		this.user = objMsg.getMentions().toString();
		this.mentionedUser = new UserConvertID(user);

		// the User ID of the User that challenged the author of the message
		this.grabChall = new LadderCheck();
		this.authorChallUser = grabChall.grabChallenger(authorUser.conversion());
	}

	/**
	 * This command challenges the mentioned user to a match, only challenges if the
	 * mentioned user is in the ladder and is a player other than yourself
	 */
	public void challengePlayer() {
		LadderCheck checkChallUser = new LadderCheck(mentionedUser.conversion(user));

		if (objMsg.getContentRaw()
				.equalsIgnoreCase(Ref.prefix + "Challenge" + " <@!" + mentionedUser.conversion() + ">")
				|| objMsg.getContentRaw()
						.equalsIgnoreCase(Ref.prefix + "Challenge" + " <@" + mentionedUser.conversion() + ">")) {
			String challAuthor = objMsg.getAuthor().toString();
			UserConvertID challAuthorUser = new UserConvertID(challAuthor);
			if (challAuthorUser.conversion().equals(mentionedUser.conversion())) {
				objMsgCh.sendMessage(objUser.getAsMention() + "You can't challenge yourself idiot").queue();

				// checking to see if the player you challenged is in the ladder
			} else if (checkChallUser.checkIfInLadder() == false) {

				objMsgCh.sendMessage(objUser.getAsMention() + " That user is not registered in the ladder ").queue();

			} else {

				// new ladder object has fresh variables as to not conflict with the other
				// ladder check
				LadderCheck checkIfFighting = new LadderCheck(mentionedUser.conversion(user));
				// checking if the player is already fighting someone else and if false doesn't
				// run the challenge code
				if (checkIfFighting.checkIfFighting() == true) {

					objMsgCh.sendMessage(
							objUser.getAsMention() + " That user is already fighting, try asking them later").queue();

				} else {

					// if the player is in the ladder it sends the fight invitation message
					objMsgCh.sendMessage(objUser.getAsMention() + " Would like to fight you " + " <@"
							+ mentionedUser.conversion(user) + ">! \n" + " Reply with " + Ref.prefix + "accept @User"
							+ " Within 1 minute to accept!").queue();

					// checking who was challenged and who issued the challenged for the timer reset

					ChallengerUpdate update = new ChallengerUpdate();
					update.updateChallenger(authorUser.conversion(), mentionedUser.conversion(user));

					// a timer to reset each players challenge values if the invitation isn't
					// accepted within the allotted time
					Timer t = new Timer();
					t.schedule(new TimerTask() {

						@Override
						public void run() {
							LadderCheck checkIfFighting = new LadderCheck(mentionedUser.conversion(user));
							/**
							 * checking if the player is already fighting so their is fighting value doesn't
							 * disappear if they accepted the challenge in time
							 */
							if (checkIfFighting.checkIfFighting() == false
									&& checkIfFighting.checkIfFighting(authorUser.conversion()) == false) {
								if (check.checkIfReset(authorUser.conversion(), mentionedUser.conversion()) == false) {
									update.reset(authorUser.conversion(), mentionedUser.conversion(user));
								}
								System.out.println("Timer done");
							}
						}
					}, 60000);
				}
			}
		}

	}

	/**
	 * accepts the Challenge if you were issued one by another player
	 */
	public void acceptChallenge() {

		if (objMsg.getContentRaw().equalsIgnoreCase(Ref.prefix + "accept" + " <@" + mentionedUser.conversion() + ">")
				|| objMsg.getContentRaw()
						.equalsIgnoreCase(Ref.prefix + "accept" + " <@!" + mentionedUser.conversion() + ">")) {
			update.updateChallenger(authorUser.conversion(), mentionedUser.conversion());
			if (check.checkIfChallenged(mentionedUser.conversion(), authorUser.conversion())) {
				objMsgCh.sendMessage(" Begin fight now " + " <@" + mentionedUser.conversion() + ">" + " VS " + "<@"
						+ authorUser.conversion() + ">\nWhen finished one user reply with" + " \"" + Ref.prefix
						+ "finish @WinnersName won\"\nThe match will time out if not finished within 30 minutes")
						.queue();

				update.updateIsFightingTrue(mentionedUser.conversion(), authorUser.conversion());
				ChallengerUpdate update = new ChallengerUpdate();
				update.updateChallenger(authorUser.conversion(), mentionedUser.conversion(user));
				Timer t = new Timer();
				t.schedule(new TimerTask() {

					@Override
					public void run() {
						/**
						 * checking if the player is already fighting so their is fighting value doesn't
						 * disappear if they accepted the challenge in time
						 */
						if (check.checkIfReset(authorUser.conversion(), mentionedUser.conversion()) == false) {
							update.reset(authorUser.conversion(), mentionedUser.conversion(user));
						}
						System.out.println("Timer done");

					}
				}, 1800000);

			}
		}

	}

	/**
	 * when a match has finished, this code if for if the loser says the winner won
	 */
	public void finishChallWon() {
		if (objMsg.getContentRaw().equalsIgnoreCase(Ref.prefix + "finish" + " <@" + authorChallUser + ">" + " won")
				|| objMsg.getContentRaw()
						.equalsIgnoreCase(Ref.prefix + "finish" + " <@!" + authorChallUser + ">" + " won")) {

			// Claim that the author of the message lost
			update.calimLoss(authorUser.conversion());
			// if player states that their opponent won
			if (check.checkMatchCreated(authorUser.conversion(), authorChallUser)) {
				objMsgCh.sendMessage(" GG's\n" + "<@" + authorChallUser + ">" + " Did"
						+ " you really win? \n Reply with \"" + Ref.prefix + "yes @yourName won\" if you did").queue();
			} else {
				objMsgCh.sendMessage(objUser.getAsMention() + " No match found, either no match wasn't created or"
						+ " it was timed out\n no worries just created a new match with " + Ref.prefix + " Challenge")
						.queue();
			}

		}
	}

	/**
	 * when a match has finished, this code if for if the winner says they won
	 */
	public void finishAuthorWon() {
		// player states that they themselves won
		if (objMsg.getContentRaw()
				.equalsIgnoreCase(Ref.prefix + "finish" + " <@" + authorUser.conversion() + ">" + " won")
				|| objMsg.getContentRaw()
						.equalsIgnoreCase(Ref.prefix + "finish" + " <@!" + authorUser.conversion() + ">" + " won")) {

			// author of the message claims that they won
			if (check.checkIfFighting(authorUser.conversion())) {
				update.calimWin(authorChallUser);
			}
			if (check.checkMatchCreated(authorUser.conversion(), authorChallUser)) {
				objMsgCh.sendMessage(
						" GG's\n" + "<@" + authorChallUser + ">" + " Did" + "<@" + mentionedUser.conversion() + ">"
								+ " really win? \n Reply with \"" + Ref.prefix + "yes @TheirName won\" if they did")
						.queue();
			} else {
				// runs this code if not match between these players was found
				objMsgCh.sendMessage(objUser.getAsMention() + " No match found, either no match wasn't created or"
						+ " it was timed out\n no worries just created a new match with " + Ref.prefix + " Challenge")
						.queue();
			}
		}
	}

	/**
	 * after a finish response to a match, this code askes the other player if the
	 * result the first player stated is true
	 */
	public void challWonResponse() {
		// checking the other player if the claims add up and either awarding points or
		// doing nothing based on weather or not the claims add up
		if (objMsg.getContentRaw().equalsIgnoreCase(Ref.prefix + "yes " + "<@" + authorChallUser + ">" + " won")
				|| objMsg.getContentRaw()
						.equalsIgnoreCase(Ref.prefix + "yes " + "<@!" + authorChallUser + ">" + " won")) {
			if (check.checkIfFighting(authorUser.conversion()) == true) {
				update.calimLoss(authorUser.conversion());
				if (check.checkClaims(authorUser.conversion(), authorChallUser) == true) {
					objMsgCh.sendMessage(objUser.getAsMention() + " Points awarded").queue();
					points.transferPoints(authorChallUser, authorUser.conversion());
					update.UpdateWin(authorChallUser);
					update.UpdateLoss(authorUser.conversion());
					update.reset(authorUser.conversion(), authorChallUser);
				} else {
					objMsgCh.sendMessage(objUser.getAsMention() + " Claims do not match, call an admin").queue();
					update.reset(authorUser.conversion(), mentionedUser.conversion());

				}
			}

		}
	}

	/**
	 * after a finish response to a match, this code askes the other player if the
	 * result the first player stated is true
	 */
	public void authorWonResponse() {
		// the response to someone finishing a match that the author won
		if (objMsg.getContentRaw().equalsIgnoreCase(Ref.prefix + "yes " + "<@" + authorUser.conversion() + ">" + " won")
				|| objMsg.getContentRaw()
						.equalsIgnoreCase(Ref.prefix + "yes " + "<@!" + authorUser.conversion() + ">" + " won")) {
			System.out.println(authorChallUser);
			if (check.checkIfFighting(authorUser.conversion()) == true) {
				update.calimWin(authorUser.conversion());
				if (check.checkClaims(authorUser.conversion(), authorChallUser) == true) {
					objMsgCh.sendMessage(objUser.getAsMention() + " Points awarded").queue();
					points.transferPoints(authorUser.conversion(), authorChallUser);
					update.UpdateWin(authorUser.conversion());
					update.UpdateLoss(authorChallUser);
					update.reset(authorUser.conversion(), authorChallUser);
				} else {
					objMsgCh.sendMessage(objUser.getAsMention() + " Claims do not match, call an admin").queue();
					update.reset(authorUser.conversion(), mentionedUser.conversion());
				}
			}
		}

	}

}
