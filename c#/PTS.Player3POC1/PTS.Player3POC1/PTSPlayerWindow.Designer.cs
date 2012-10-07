namespace PTS.Player3POC1
{
    partial class PTSPlayerWindow
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.SuspendLayout();
            // 
            // PTSPlayerWindow
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(784, 562);
            this.Name = "PTSPlayerWindow";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "PTS Player 3 poc 1";
            this.FormClosing += new System.Windows.Forms.FormClosingEventHandler(this.PTSPlayerWindow_FormClosing);
            this.FormClosed += new System.Windows.Forms.FormClosedEventHandler(this.PTSPlayerWindow_FormClosed);
            this.Load += new System.EventHandler(this.PTSPlayerWindow_Load);
            this.ResumeLayout(false);

        }

        #endregion
    }
}

