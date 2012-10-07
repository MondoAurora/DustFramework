
using System.Windows.Forms;
using System;
using Dust.Core;
using System.Drawing;

namespace Dust.Gui
{
    public class GuiText : GuiComponent<DGuiText>
    {
        protected Label lbl = new Label();
        DustData obAct;

        public GuiText() { }

        public GuiText(String text, DustData obAct = null)
        {
            setText(text);
            setData(obAct);
        }

        protected override Control initI()
        {
            IGuiText d = getData();

            setData(d.getActivationData());
            setText(d.getText());
            return lbl;
        }

        void setData(DustData obAct)
        {
            this.obAct = obAct;

            if (null != obAct)
            {
                lbl.Font = new Font(lbl.Font, FontStyle.Bold);
            }
        }

        void setText(String txt)
        {
            lbl.Text = txt;
            lbl.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
        }

        protected override bool isActive() { return null != obAct; }
        protected override DustData getActivatedObject() { return obAct; }
    }
}
