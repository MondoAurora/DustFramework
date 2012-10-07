using System.Collections.Generic;

using System.Windows.Forms;
using System;
using Dust.Gui;
using Dust.Tools;
using Dust.Core;


namespace Dust.Media
{
    public class MediaGuiContainer : GuiComponent<DMediaGuiContainer>
    {
        DGuiText unknownType;
        Dictionary<EMediaType, DMediaGuiComponent> dictDisplays = new Dictionary<EMediaType, DMediaGuiComponent>();

        DGuiFlip theFlip;
        DToolsListSelector thePanels;

        DustData actDisplay = null;

        protected override Control initI()
        {
            unknownType = new DGuiText() { text = "Unknown media type" };

            IToolsList ehl = getData().getEventHandlers();

            dictDisplays[EMediaType.HTML] = new DMediaGuiHtml() { eventHandlers = ehl };
            dictDisplays[EMediaType.Image] = new DMediaGuiImage() { eventHandlers = ehl };
            dictDisplays[EMediaType.Video] = new DMediaGuiVideo() { eventHandlers = ehl };

            thePanels = new DToolsListSelector()
            {
                list = new DToolsList(new DustData[] {
                    unknownType, dictDisplays[EMediaType.HTML], 
                    dictDisplays[EMediaType.Image], dictDisplays[EMediaType.Video]
                })
            };
            theFlip = new DGuiFlip()
            {
                content = thePanels
            };

            update();

            listen(new DustMessageFilter((DustCompData)getData().getMediaList()));

            return theFlip.getGuiCtrl();
        }

        protected override void processGui(DustMessage message, DustData source)
        {
            if (message.getMessageId() is EToolsListSelectorMessage)
            {
                update();
            }
        }

        public void update()
        {
            IMediaItem mi = (IMediaItem)getData().getMediaList().getSelectedSingle();
            EMediaType mt = (null == mi) ? EMediaType.Unknown : mi.getType();
            DustCompData sel = (EMediaType.Unknown == mt) ? (DustCompData)unknownType : dictDisplays[mt];

            if (EMediaType.Unknown != mt)
            {
                if (mi != dictDisplays[mt].getMediaItem())
                {
                    send(new MMediaGuiComponentSetMedia() { item = mi }, sel);
                }
            }

            if (sel != actDisplay)
            {
                send(new MToolsListSelectorSelect(sel), thePanels);
            }
        }
    }
}
