using System;
using System.Collections.Generic;

namespace Dust.Core
{
    [Serializable()]
    public class DustIdentifier
    {
        public String localName;

        [NonSerialized]
        DustIdentifier parent;
        [NonSerialized]
        String path;

        public DustIdentifier() { }

        public DustIdentifier(String id, DustIdentifier parent = null)
        {
            this.localName = id;
            setParent(parent);
        }

        public void setParent(DustIdentifier parent)
        {
            this.parent = parent;
            update();
        }

        void update()
        {
            if (null == parent)
            {
                path = localName;
            }
            else
            {
                path = parent.ToString() + "." + localName;
            }
        }

        public String getName()
        {
            return localName;
        }

        public override string ToString()
        {
            return path;
        }
    }
}
