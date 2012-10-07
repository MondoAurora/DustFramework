
using System;
using System.Windows.Forms;
using Dust.Gui;


namespace Dust.Media
{
    public class MediaGuiHtml : MediaGuiComponent<DMediaGuiHtml>
    {
        WebBrowser browser;

        protected override Control initI()
        {
            browser = new WebBrowser();

            setInitialContent();

            browser.Navigating += navigating;

            return browser;
        }

        protected virtual void navigating(object sender, EventArgs e)
        {
            dispatchCompMsg(EGuiComponentMessage.Interacted, getActivatedObject());
        }

        protected override void setMediaI(IMediaItem item)
        {
            browser.Url = (null == item) ? null : item.getMediaUrl();
        }
    }
}
