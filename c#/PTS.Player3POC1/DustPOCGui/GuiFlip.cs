
using System.Windows.Forms;
using System.Collections.Generic;
using Dust.Tools;
using Dust.Core;


namespace Dust.Gui
{
    public class GuiFlip : GuiComponent <DGuiFlip>
    {
        Panel pnl;

        protected override Control initI() {
            pnl = new Panel();

            IToolsListSelector selector = getData().getContent();
            Control c;

            foreach (DustData d in selector.getList().getItems())
            {
                // initialize all members in this init to have the same owner thread!
                c = ((IGuiComponent)d).getGuiCtrl();
                if ( selector.isSelected(d) ) {
                    addCtrl(c);
                }
            }

            listen(new DustMessageFilter((DustCompData)selector));

            return pnl;
        }

        protected override void processGui(DustMessage message, DustData source)
        {
            if ((message.getMessageId() is EToolsListSelectorMessage) && (source == getData().getContent()))
            {
                switch ((EToolsListSelectorMessage)message.getMessageId())
                {
                    case EToolsListSelectorMessage.ClearSelection:
                        clear();
                        break;
                    case EToolsListSelectorMessage.SelectNext:
                        clear();
                        addCtrl(((IGuiComponent)getData().getContent().getSelected().GetEnumerator().Current).getGuiCtrl());
                        break;
                    case EToolsListSelectorMessage.Select:
                        clear();
                        foreach (DustData d in ((MIToolsListBase)message).getItems())
                        {
                            if (-1 != getData().getContent().getList().indexOf(d))
                            {
                                addCtrl(((IGuiComponent)d).getGuiCtrl());
                            }
                        }
                        break;
                    case EToolsListSelectorMessage.Unselect:
                        foreach (DustData d in ((MIToolsListBase)message).getItems())
                        {
                            removeCtrl(((IGuiComponent)d).getGuiCtrl());
                        }
                        break;
                }
            }
        }

        protected void addCtrl(Control ctrl)
        {
            ctrl.Visible = true;
            ctrl.BringToFront();
            ctrl.Dock = DockStyle.Fill;
            pnl.Controls.Add(ctrl);
        }

        protected void clear()
        {
            foreach (Control ctrl in pnl.Controls)
            {
                ctrl.Visible = false;
            }
            pnl.Controls.Clear();
        }

        protected void removeCtrl(Control ctrl)
        {
            ctrl.Visible = false;
            pnl.Controls.Remove(ctrl);
        }
    }
}
