
using System.Windows.Forms;
using System.Windows.Controls;
using System.Windows.Forms.Integration;
using Dust.Gui;
using Dust.Tools;
using Dust.Core;
using System.Windows.Threading;
using System;


namespace Dust.Media
{
    public class MediaGuiVideo : MediaGuiComponent<DMediaGuiVideo>
    {
        ElementHost eh;
        MediaElement movie;

        public MediaGuiVideo()
        {
            eh = new ElementHost();
            movie = new MediaElement();
            movie.LoadedBehavior = MediaState.Manual;
            movie.UnloadedBehavior = MediaState.Manual;

            movie.MediaEnded += endHandler;

            movie.MouseUp += click;

            eh.Child = movie;
        }

        protected override System.Windows.Forms.Control initI()
        {
            setInitialContent();

            return eh;
        }

        private delegate void SetMediaUriDelegate(MediaElement e, Uri u);

        protected static void SetMediaUri(MediaElement e, Uri u)
        {
            e.Source = u;
            e.Play();
        }

        protected override void setMediaI(IMediaItem item)
        {
            SetMediaUriDelegate proctDelegate = new SetMediaUriDelegate(SetMediaUri);

            movie.Dispatcher.BeginInvoke(proctDelegate, new object[] { movie, item.getMediaUrl() });

            //            movie.Dispatcher.BeginInvoke(DispatcherPriority.Normal, new Action<MediaElement, Uri>(SetMediaUri), new object[]{movie, item.getMediaUrl()});

            //            movie.Source = item.getMediaUrl();
            //            movie.Play();
        }

        public void endHandler(object sender, System.EventArgs e)
        {

        }

        public override void hidden()
        {
            movie.Stop();
        }

    }
}
