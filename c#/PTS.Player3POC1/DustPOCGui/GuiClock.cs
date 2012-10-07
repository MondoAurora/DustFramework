
using System.Windows.Forms;
using System;
using Dust.Core;
using Dust.Tools;

namespace Dust.Gui
{
    public class GuiClock : GuiComponent<DGuiClock>
    {
        protected Label lbl = new Label();
        protected String timeFormat = "HH:mm:ss";

        private String strTime;

        public GuiClock() { }

        protected override Control initI() {
            setTimeFormat(getData().getTimeFormat());

            update(DateTime.Now);
            listen(EToolsSchedulerMessage.HeartBeat);

            return lbl;
        }

        void setTimeFormat(String format)
        {
            this.timeFormat = format;
        }

        void update(DateTime t)
        {
            String st = t.ToString(timeFormat);

            if (!st.Equals(strTime))
            {
                strTime = st;

                lbl.Text = st;
                lbl.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
            }
        }

        protected override void processGui(DustMessage message, DustData source)
        {
            if (EToolsSchedulerMessage.HeartBeat.Equals(message.getMessageId()))
            {
                update(((MToolsSchedulerHeartBeat)message).getHeartBeatTime());
            }
        }
    }
}
