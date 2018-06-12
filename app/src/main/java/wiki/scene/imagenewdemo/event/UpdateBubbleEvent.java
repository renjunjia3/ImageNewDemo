package wiki.scene.imagenewdemo.event;

import wiki.scene.imagenewdemo.entity.BubbleLocalTemplateInfo;

public class UpdateBubbleEvent {
    private BubbleLocalTemplateInfo info;

    public UpdateBubbleEvent(BubbleLocalTemplateInfo info) {
        this.info = info;
    }

    public BubbleLocalTemplateInfo getInfo() {
        return info;
    }
}
