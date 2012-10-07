using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Dust.Core
{
    public abstract class DustComponentBase
    {
        internal static IDustEnvironment env;

        public enum ProcessReturn { Unhandled, Acknowledged, Updated }

        internal abstract void setData(DustCompData data);
        public abstract DustCompData getDataDef();

        protected bool isStopping() { return env.isEnvStopping(); }

        protected static IDustEnvironment getEnv()
        {
            return env;
        }
        
        protected void send(DustMessage message, DustCompData to = null)
        {
            env.send(message, getDataDef(), to);
        }

        protected void listen(Enum msgId)
        {
            listen(new DustMessageFilter(msgId));
        }

        protected void listen(DustCompData source)
        {
            listen(new DustMessageFilter(source));
        }

        protected void listen(DustMessage.Filter filter)
        {
            env.listen(filter, this);
        }

        internal void setup(DustCompData data)
        {
            setData(data);
            init();
        }

        public virtual void init() { }

        public virtual ProcessReturn process(DustMessage message, DustCompData source) {
            return ProcessReturn.Unhandled;
        }
    }

    public abstract class DustComponent <DataType> : DustComponentBase where DataType : DustCompData
    {
        internal DataType data = null;

        public override DustCompData getDataDef() { return data; }
        public DataType getData() { return data; }

        internal sealed override void setData(DustCompData data)
        {
            this.data = (DataType)data;
        }
    }

    public abstract class DustComponentDefault : DustComponent<DustCompData> { }
}
