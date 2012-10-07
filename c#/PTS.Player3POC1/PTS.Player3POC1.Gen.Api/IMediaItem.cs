using System;

using Dust.Core;
using Dust.Tools;

namespace Dust.Media
{
    public enum EMediaType { Unknown = 0, Image = 1, HTML = 2, Video = 3 };

    public interface IMediaItem
    {
        Uri getMediaUrl();
        String getMediaPath();
        EMediaType getType();
        IMediaItem getReferredItem();
    }

    public interface IMediaItemRefSelectionAction : IToolsAction 
    {
        DustCompData getTarget();
    }
}
