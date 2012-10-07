
using System.Windows.Forms;
using Dust.Gui;
using Dust.Core;

namespace Dust.Media
{
    public abstract class MediaGuiComponent <DataType>: GuiComponent <DataType> where DataType : DMediaGuiComponent
    {
        protected override bool isActive() { return true; }
        protected override DustData getActivatedObject() { return (DustData) getData().item; }

        protected void setInitialContent()
        {
            IMediaItem mi = getData().getMediaItem();

            if (null != mi)
            {
                setMediaI(mi);
            }
        }

        public void setMedia(IMediaItem item)
        {
            DMediaGuiComponent d = getData();

            if (d.item != item)
            {
                getData().item = (DMediaItem)item;
                setMediaI(item);
            }
        }

        protected abstract void setMediaI(IMediaItem item);

        protected override void processGui(DustMessage message, DustData source)
        {
            if (message is MIMediaGuiComponentSetMedia)
            {
                setMedia(((MIMediaGuiComponentSetMedia)message).getMedia());
            }
        }
    }
}
