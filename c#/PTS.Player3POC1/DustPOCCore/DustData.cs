using System;
using System.Collections.Generic;

namespace Dust.Core
{
    [Serializable()]
    public abstract class DustData
    {
        public DustIdentifier id;

        public DustData() { }

        public DustData(String idStr)
        {
            this.id = new DustIdentifier(idStr);
        }

        public virtual bool isComp() { return false; }

        public sealed class Temp : DustData
        {
        }
    }

    public abstract class DustMessage : DustData
    {
        public abstract Enum getMessageId();

        public interface Filter
        {
            bool accept(DustMessage msg, DustCompData source, DustCompData target);
        }
    }

    [Serializable()]
    public class DustMessageFilter : DustMessage.Filter
    {
        DustCompData source;
        Enum msgId;
        DustCompData target;

        public DustMessageFilter(DustCompData source, Enum msgId = null, DustCompData target = null)
        {
            this.source = source;
            this.msgId = msgId;
            this.target = target;
        }

        public DustMessageFilter(Enum msgId) : this(null, msgId, null) { }

        public bool accept(DustMessage msg, DustCompData source, DustCompData target)
        {
            return ((null == msgId) || (msgId.Equals(msg.getMessageId())))
                && ((null == this.source) || (this.source == source))
                && ((null == this.target) || (this.target == target));
        }
    }

    public abstract class DustCompData : DustData
    {
        private DustComponentBase comp;

        public DustCompData() { }

        public DustCompData(String idStr) : base(idStr) { }

        public override bool isComp() { return true; }

        public void ping() { getComp(); }

        protected internal DustComponentBase getComp()
        {
            if (null == comp)
            {
                comp = DustComponentBase.env.createComponent(this);
            }

            return comp;
        }
    }
}
