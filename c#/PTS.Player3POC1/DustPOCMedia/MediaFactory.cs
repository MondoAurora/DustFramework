using Dust.Core;

namespace Dust.Media
{
    public class MediaFactory : DustFactory
    {
        public DustComponentBase createComponentForData(DustCompData data)
        {
            if (data is IMediaGuiHtml)
            {
                return new MediaGuiHtml();
            }
            else if (data is IMediaGuiImage)
            {
                return new MediaGuiImage();
            }
            else if (data is IMediaGuiVideo)
            {
                return new MediaGuiVideo();
            }
            else if (data is IMediaGuiContainer)
            {
                return new MediaGuiContainer();
            }
            else if (data is IMediaItemRefSelectionAction)
            {
                return new MediaItemRefSelectionAction();
            } 

            
            return null;
        }
    }
}
