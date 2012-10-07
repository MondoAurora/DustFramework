using System;
using System.Windows.Forms;
using PTS.Player3POC1.Demo2;

namespace PTS.Player3POC1
{
    public partial class PTSPlayerWindow : Form
    {
        Demo2Player player;

        public PTSPlayerWindow()
        {
            InitializeComponent();
        }

        private void PTSPlayerWindow_Load(object sender, EventArgs e)
        {
            player = new Demo2Player();
            player.init(this);
        }

        private void PTSPlayerWindow_FormClosing(object sender, FormClosingEventArgs e)
        {
        }

        private void PTSPlayerWindow_FormClosed(object sender, FormClosedEventArgs e)
        {
            player.shutdown();
        }
    }
}
