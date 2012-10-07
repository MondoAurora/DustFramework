
using System.Windows.Forms;
using System;
using Dust.Core;
using System.Diagnostics;
using Dust.Tools;
using System.Windows;
using System.Windows.Threading;
using System.Collections.Generic;

namespace Dust.Gui
{
    public abstract class GuiComponent<DataType> : DustComponent<DataType> where DataType : DGuiComponent
    {
        Dictionary<EGuiComponentMessage, DToolsAction> eventHandlers = new Dictionary<EGuiComponentMessage, DToolsAction>();

        public virtual void displayed() { }
        public virtual void hidden() { }

        public sealed override void init()
        {
            getData().ctrl = initI();

            Control ctrl = getData().ctrl;

            if (null != ctrl)
            {
                if (isActive())
                {
                    ctrl.Click += click;
                }

                ctrl.VisibleChanged += visChg;
            }

            IToolsList ehl = getData().getEventHandlers();
            if (null != ehl)
            {
                foreach (DustData eh in ehl.getItems())
                {
                    eventHandlers[(EGuiComponentMessage)Enum.Parse(typeof(EGuiComponentMessage), eh.id.getName())] = (DToolsAction)eh;
                }
            }
        }

        protected abstract Control initI();

        protected virtual bool isActive() { return false; }
        protected virtual DustData getActivatedObject() { return null; }

        protected void dispatchCompMsg(EGuiComponentMessage msg, DustData par)
        {
            DToolsAction action;
            if (eventHandlers.TryGetValue(msg, out action))
            {
                MToolsActionExecute msgExec = new MToolsActionExecute() { param = par };
                send(msgExec, action);
            }

            switch (msg)
            {
                case EGuiComponentMessage.Activated:
                    send(new MGuiComponentActivated(par));
                    break;
                case EGuiComponentMessage.Displayed:
                    break;
                case EGuiComponentMessage.Hidden:
                    break;
                case EGuiComponentMessage.Interacted:
                    break;
            }
        }

        protected virtual void click(object sender, EventArgs e)
        {
            dispatchCompMsg(EGuiComponentMessage.Activated, getActivatedObject());
        }

        protected virtual void visChg(object sender, EventArgs e)
        {
            Control ctrl = getData().ctrl;
            bool visible = ctrl.Visible;
            for ( ; (null != ctrl) && (visible = ctrl.Visible); ctrl = ctrl.Parent)
                ;

            dispatchCompMsg(visible ? EGuiComponentMessage.Displayed : EGuiComponentMessage.Hidden, null);
        }

        protected virtual void processGui(DustMessage message, DustData source)
        {
        }

        private delegate void ProcessDelegate(DustMessage message, DustData source);

        public override sealed ProcessReturn process(DustMessage message, DustCompData source)
        {
            if (!isStopping())
            {
                Control ctrl = getData().getGuiCtrl();

                if (ctrl.InvokeRequired)
                {
                    ProcessDelegate proctDelegate = new ProcessDelegate(processGui);
                    if (!isStopping())
                    {
                        try
                        {
                            ctrl.BeginInvoke(proctDelegate, new object[] { message, source });
                        }
                        catch (Exception e)
                        {
                            Debug.WriteLine("Invoke failed " + e.ToString());
                            // now do nothing, this is the result of the very last GUI component message dispatch
                        }
                    }
                }
                else
                {
                    processGui(message, source);
                }
            }
            return ProcessReturn.Acknowledged;
        }
    }
}
