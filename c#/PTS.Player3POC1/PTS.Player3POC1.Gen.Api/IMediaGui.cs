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
    public enum EMediaGuiComponentMessage { SetMedia };

    public interface IMediaGuiComponent : IGuiComponent
    {
        IMediaItem getMediaItem();
    }

    public interface MIMediaGuiComponentSetMedia
    {
        IMediaItem getMedia();
    }

    public interface IMediaGuiHtml : IMediaGuiComponent { }
    public interface IMediaGuiImage : IMediaGuiComponent { }
    public interface IMediaGuiVideo : IMediaGuiComponent { }

    public interface IMediaGuiContainer : IGuiComponent
    {
        IToolsListSelector getMediaList();
    }

}
