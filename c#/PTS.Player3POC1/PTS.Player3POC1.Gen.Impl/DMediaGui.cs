using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using Dust.Core;
using Dust.Gui;
using Dust.Tools;

namespace Dust.Media
{
    public abstract class DMediaGuiComponent : DGuiComponent, IMediaGuiComponent
    {
        public IMediaItem item;

        public IMediaItem getMediaItem()
        {
            return item;
        }
    }

    public class MMediaGuiComponentSetMedia : DustMessage, MIMediaGuiComponentSetMedia
    {
        public IMediaItem item;

        public IMediaItem getMedia()
        {
            return item;
        }

        public override Enum getMessageId() { return EMediaGuiComponentMessage.SetMedia; }
    }


    public class DMediaGuiHtml : DMediaGuiComponent, IMediaGuiHtml { }
    public class DMediaGuiImage : DMediaGuiComponent, IMediaGuiImage { }
    public class DMediaGuiVideo : DMediaGuiComponent, IMediaGuiVideo { }

    public class DMediaGuiContainer : DGuiComponent, IMediaGuiContainer
    {
        public IToolsListSelector mediaList;

        public IToolsListSelector getMediaList()
        {
            return mediaList;
        }
    }
}
