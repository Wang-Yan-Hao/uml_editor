#!/bin/bash

# Update package lists
sudo apt update

# Upgrade installed packages
sudo apt upgrade -y

# Install the default JDK (Java Development Kit)
sudo apt install default-jdk -y

# Check if Java is installed and get the JAVA_HOME path
if command -v java >/dev/null 2>&1; then
  JAVA_HOME=$(readlink -f $(which javac) | sed 's:/bin/javac::')
  echo "Java has been installed. JAVA_HOME is: $JAVA_HOME"

  # Add JAVA_HOME and bin directory to the current shell's environment
  export JAVA_HOME="$JAVA_HOME"
  export PATH="$PATH:$JAVA_HOME/bin"
  echo "JAVA_HOME and /bin have been added to the current shell's environment."

  # Optionally, make these changes permanent by adding them to ~/.bashrc
  if grep -q "export JAVA_HOME=" ~/.bashrc; then
    echo "JAVA_HOME already defined in ~/.bashrc."
  else
    echo "export JAVA_HOME=\"$JAVA_HOME\"" >> ~/.bashrc
  fi

  if grep -q "export PATH=.*\$JAVA_HOME/bin" ~/.bashrc; then
    echo "PATH already includes \$JAVA_HOME/bin in ~/.bashrc."
  else
    echo "export PATH=\"\$PATH:\$JAVA_HOME/bin\"" >> ~/.bashrc
  fi

  echo "Changes to ~/.bashrc have been made. Run 'source ~/.bashrc' to apply them permanently in future sessions."

  # Verify the installation
  java -version
  javac -version
else
  echo "Failed to install Java."
  exit 1
fi

echo "Java installation script completed successfully."cho "Java installation script completed successfully."