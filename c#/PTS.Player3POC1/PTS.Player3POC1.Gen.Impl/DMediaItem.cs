using System;

using Dust.Core;
using Dust.Tools;

namespace Dust.Media
{
    public class DMediaItem : DustCompData, IMediaItem
    {
        public EMediaType type;
        public String path;

        public IMediaItem referred;

        private static String currentDir = Environment.CurrentDirectory.Replace('\\', '/');

        public DMediaItem() { }

        public DMediaItem(EMediaType type_, String path_, IMediaItem referred_ = null)
        {
            type = type_;
            path = path_;
            referred = referred_;
        }

        public Uri getMediaUrl()
        {
            if (-1 == path.IndexOf("://"))
            {
                path = "file://" + currentDir + "/" + path.Replace('\\', '/');
            }

            return new Uri(path);
        }

        public String getMediaPath()
        {
            return path;
        }

        public EMediaType getType()
        {
            return type;
        }

        public IMediaItem getReferredItem()
        {
            return referred;
        }
    }


    public class DMediaItemRefSelectionAction : DustCompData, IMediaItemRefSelectionAction
    {
        public DustCompData target;

        public DustCompData getTarget()
        {
            return target;
        }
    }
}
