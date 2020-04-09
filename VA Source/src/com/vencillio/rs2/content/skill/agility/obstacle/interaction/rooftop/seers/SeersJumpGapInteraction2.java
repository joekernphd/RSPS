package com.vencillio.rs2.content.skill.agility.obstacle.interaction.rooftop.seers;

import com.vencillio.core.task.Task;
import com.vencillio.core.task.TaskQueue;
import com.vencillio.core.task.impl.ForceMoveTask;
import com.vencillio.rs2.content.skill.agility.obstacle.interaction.ObstacleInteraction;
import com.vencillio.rs2.entity.Animation;
import com.vencillio.rs2.entity.Location;
import com.vencillio.rs2.entity.player.Player;

public interface SeersJumpGapInteraction2 extends ObstacleInteraction {

	@Override
	public default void start(Player player) {
		player.getUpdateFlags().sendFaceToDirection(player.getX(), player.getY() - 1);
	}

	@Override
	public default void onExecution(Player player, Location start, Location end) {
		player.teleport(new Location(player.getX(), player.getY(), player.getZ() - 1));
		TaskQueue.queue(new ForceMoveTask(player, 0, player.getLocation(), new Location(0, -3), 5043, 0, 12, 2) {
			@Override
			public void onStop() {
				super.onStop();
				TaskQueue.queue(new Task(player, 1, true) {
					int ticks = 0;

					@Override
					public void execute() {
						switch (ticks++) {
						case 3:
							player.teleport(new Location(player.getX(), player.getY(), player.getZ() + 1));
							break;
							
						case 4:
							player.teleport(end);
							player.getUpdateFlags().sendAnimation(new Animation(65535));
							stop();
							break;
						}
					}

					@Override
					public void onStop() {
					}
				});
			}
		});
	}

	@Override
	public default void onCancellation(Player player) {
		// Climbing has nothing special on cancellation.
	}
}