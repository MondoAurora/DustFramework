
using System.Windows.Forms;
using System.Drawing;
using System.Drawing.Imaging;
using System.IO;
using Dust.Gui;
using Dust.Tools;
using Dust.Core;


namespace Dust.Media
{
    public class MediaGuiImage : MediaGuiComponent<DMediaGuiImage>
    {
        PictureBox pbox;

        protected override Control initI()
        {
            pbox = new PictureBox();
            pbox.SizeMode = PictureBoxSizeMode.Zoom;

            setInitialContent();

            return pbox;
        }

        protected override void setMediaI(IMediaItem item)
        {
            pbox.Image = Image.FromFile(item.getMediaPath());
         }
    }
}
