package controller;

import model.GameObject;
import model.Tile;
import model.util.Util;
import model.Vector2;
import view.stages.Gameplay;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

public class Minimap implements MouseMotionListener {
	Gameplay gameplay;
	private ArrayList<GameObject> entities = new ArrayList<GameObject>();
	private Dimension levelSizeGU = new Dimension();
	// TODO Define size
	private Dimension minimapSizeSU = new Dimension(300, 300);

	public Minimap(Gameplay gameplay) {
		this.gameplay = gameplay;
		// TODO Listen for mouse events
	}

	// TODO Render minimap
	public void paint(Graphics g) {

		double w, h, x, y;
		double xScale = (minimapSizeSU.width / levelSizeGU.getWidth());
		double yScale = (minimapSizeSU.height / levelSizeGU.getHeight());

		if (gameplay.getLevel() != null) {
			g.setColor(Color.black);
			g.fillRect(0, 0, (int) minimapSizeSU.getWidth(), (int) minimapSizeSU.getHeight());

			// Draw tiles to minimap
			for (Tile[] ta : gameplay.getLevel().getTileMap().getTiles()) {
				for (Tile t : ta) {
					g.setColor(new Color(t.getColor().toIntBits()));
					w = (Util.getTileSize() * xScale);
					h = (Util.getTileSize() * xScale);
					x = (t.getX() * xScale);
					y = (minimapSizeSU.height - (t.getY() * yScale + h)) + 1;
					g.fillRect((int) x, (int) y, (int) w, (int) h);
				}
			}

			// Draw entities to minimap
			g.setColor(Color.RED);
			for (GameObject e : entities) {
				w = (e.getSprite().getWidth() * xScale);
				h = (e.getSprite().getHeight() * yScale);
				x = (e.getX() * xScale) - w / 2;
				y = ((minimapSizeSU.height - e.getY() * yScale) - h / 2) + 1;
				g.fillOval((int) x, (int) y, (int) w, (int) h);
			}

			// Draw camera to minimap
			g.setColor(Color.YELLOW);
			Vector2 botLeft = Util.getCamBottomLeft(gameplay.getCamera());
//			Util.flipY(botLeft);
			w = (gameplay.getCamera().viewportWidth * xScale) + 1;
			h = (gameplay.getCamera().viewportHeight * yScale) + 1;
			x = (botLeft.x * xScale);
			y = (botLeft.y * yScale);
			g.drawRect((int) x, (int) y, (int) w, (int) h);
		}
	}

	public void registerEntity(GameObject e) {
		entities.add(e);
	}

	public void unregisterEntity(GameObject e) {
		entities.remove(e);
	}

	public void updateSize() {
		this.levelSizeGU.width = (int) (gameplay.getLevel().getTileMap().getSize().getWidth() * 50) - 1;
		this.levelSizeGU.height = (int) (gameplay.getLevel().getTileMap().getSize().getHeight() * 50) - 1;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		float xScale = (float) (levelSizeGU.getWidth() / minimapSizeSU.width);
		float yScale = (float) (levelSizeGU.getHeight() / minimapSizeSU.height);
		Point worldClick = e.getPoint();
		Util.flipY(worldClick, minimapSizeSU.height);
		worldClick.x = (int) (worldClick.x * (xScale));
		worldClick.y = (int) (worldClick.y * (yScale));
		gameplay.getCamera().position.set(worldClick.x, worldClick.y, 0);
	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}
}