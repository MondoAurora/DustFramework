using System;
using Dust.Core;
using System.Windows.Forms;
using Dust.Tools;

namespace Dust.Gui
{
    public class DGuiComponent : DustCompData, IGuiComponent
    {
        public IToolsList eventHandlers;

        [NonSerialized]
        public Control ctrl;

        public Control getGuiCtrl()
        {
            if (null == ctrl)
            {
                getComp(); // this will create the component, call init which should set ctrl
            }
            return ctrl;
        }

        public IToolsList getEventHandlers()
        {
            return eventHandlers;
        }
    }

    public class MGuiComponentActivated : DustMessage, MIGuiComponentActivated
    {
        public DustData activatedObject;

        public MGuiComponentActivated(DustData activatedObject)
        {
            this.activatedObject = activatedObject;
        }

        public override Enum getMessageId() { return EGuiComponentMessage.Activated; }

        public DustData getActivatedObject()
        {
            return activatedObject;
        }
    }

    public class MGuiComponentInteracted : DustMessage
    {
        public override Enum getMessageId() { return EGuiComponentMessage.Interacted; }
    }

    public class MGuiComponentDisplayed : DustMessage
    {
        public override Enum getMessageId() { return EGuiComponentMessage.Displayed; }
    }

    public class MGuiComponentHidden : DustMessage
    {
        public override Enum getMessageId() { return EGuiComponentMessage.Hidden; }
    }

    public class DGuiMarquee : DGuiComponent { }
}
