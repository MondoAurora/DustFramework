
using System.Windows.Forms;
using System;
using Dust.Core;
using System.Drawing;
using Dust.Tools;

namespace Dust.Gui
{
    public class GuiMarquee : GuiComponent <DGuiMarquee>
    {
        protected Panel pnl;
        protected Label[] lbls;
        DustData obAct;

        int waitIntSec = 8;
        DateTime intChgTime;

        int current;

        public GuiMarquee(String[] texts)
        {
            pnl = new Panel();

            this.obAct = null;

            lbls = new Label[texts.Length];
            int i = 0;
            Label l;
            foreach ( String txt in texts ) {
                l = new Label();
                l.Text = txt;
                l.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
                l.Dock = DockStyle.Fill;
                l.Visible = true;

                lbls[i++] = l;
            }

            pnl = new Panel();

            select(0);
        }

        protected override Control initI()
        {
            listen(EToolsSchedulerMessage.HeartBeat);
            return pnl;
        }

        void select(int i)
        {
            current = i;
            pnl.Controls.Clear();
            pnl.Controls.Add(lbls[i]);
            intChgTime = DateTime.Now.AddSeconds(waitIntSec);
        }

        void update(DateTime t)
        {
            if (intChgTime < t)
            {
                select((current + 1) % lbls.Length);
            }
        }

        protected override bool isActive() { return true; }
        protected override DustData getActivatedObject() { return obAct; }
    }
}
