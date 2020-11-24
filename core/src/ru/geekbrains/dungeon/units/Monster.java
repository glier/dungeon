package ru.geekbrains.dungeon.units;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import ru.geekbrains.dungeon.GameController;
import ru.geekbrains.dungeon.Utils;

public class Monster extends Unit {
    private float aiBrainsImplseTime;
    private Unit target;

    public Monster(TextureAtlas atlas, GameController gc) {
        super(gc, 5, 2, 10);
        this.texture = atlas.findRegion("monster");
        this.textureHp = atlas.findRegion("hp");
        this.hp = -1;
    }

    public void activate(int cellX, int cellY) {
        this.cellX = cellX;
        this.cellY = cellY;
        this.targetX = cellX;
        this.targetY = cellY;
        this.hpMax = 10;
        this.hp = hpMax;
        this.target = gc.getUnitController().getHero();
    }

    public void update(float dt) {
        super.update(dt);
        if (canIMakeAction()) {
            if (isStayStill()) {
                aiBrainsImplseTime += dt;
            }
            think(dt);
        }
    }

    public void think(float dt) {
        if (aiBrainsImplseTime > 0.4f) {
            aiBrainsImplseTime = 0.0f;
            if (canIAttackThisTarget(target)) {
                attack(target);
                return;
            }
            if(amIBlocked()) {
                turns = 0;
                return;
            }
            if(Utils.getCellsIntDistance(cellX, cellY, target.getCellX(), target.getCellY()) < 5) {
                tryToMove(target.getCellX(), target.getCellY());
            } else {
                int dx, dy;
                do {
                    dx = MathUtils.random(0, gc.getGameMap().getCellsX() - 1);
                    dy = MathUtils.random(0, gc.getGameMap().getCellsY() - 1);
                } while (!(isCellEmpty(dx, dy)) && Utils.isCellAreNeighbours(cellX, cellY, dx, dy));
                tryToMove(dx, dy);
            }
        }
    }

    public void tryToMove(int preferredX, int preferredY) {
        int bestX = -1, bestY = -1;
        float bestDst = 10000;
        for (int i = cellX - 1; i <= cellX + 1; i++) {
            for (int j = cellY - 1; j <= cellY + 1; j++) {
                if (Utils.isCellAreNeighbours(cellX, cellY, i, j) && isCellEmpty(i, j)) {
                    float dst = Utils.getCellsFloatDistance(preferredX, preferredY, i, j);
                    if (dst < bestDst) {
                        bestDst = dst;
                        bestX = i;
                        bestY = j;
                    }
                }
            }
        }
        goTo(bestX, bestY);
    }
}
