from PyQt5 import QtCore, QtGui, QtWidgets


class Msg(object):
	def __init__(self, msg):
		"""
		Simple Dialog with a message and an OK button which closes it
		:param msg: The message which will be displayed in the small dialog
		"""
		self._msg = msg
		self._dialog = QtWidgets.QDialog()

	def setupUi(self):
		"""
		Auto generated!
		Seriously, this auto generated code stuffs is weird
		"""
		self._dialog.setObjectName("Dialog")
		self._dialog.resize(280, 120)
		self.verticalLayoutWidget = QtWidgets.QWidget(self._dialog)
		self.verticalLayoutWidget.setGeometry(QtCore.QRect(9, 9, 261, 101))
		self.verticalLayoutWidget.setObjectName("verticalLayoutWidget")
		self.verticalLayout = QtWidgets.QVBoxLayout(self.verticalLayoutWidget)
		self.verticalLayout.setContentsMargins(0, 0, 0, 0)
		self.verticalLayout.setObjectName("verticalLayout")
		self.labelMsg = QtWidgets.QLabel(self.verticalLayoutWidget)
		font = QtGui.QFont()
		font.setFamily("Arial")
		font.setPointSize(12)
		self.labelMsg.setFont(font)
		self.labelMsg.setLayoutDirection(QtCore.Qt.LeftToRight)
		self.labelMsg.setAlignment(QtCore.Qt.AlignCenter)
		self.labelMsg.setObjectName("labelMsg")
		self.verticalLayout.addWidget(self.labelMsg)
		self.horizontalLayout = QtWidgets.QHBoxLayout()
		self.horizontalLayout.setObjectName("horizontalLayout")
		spacerItem = QtWidgets.QSpacerItem(40, 20, QtWidgets.QSizePolicy.Expanding, QtWidgets.QSizePolicy.Minimum)
		self.horizontalLayout.addItem(spacerItem)
		self.btnOK = QtWidgets.QPushButton(self.verticalLayoutWidget)
		self.btnOK.setObjectName("btnOK")
		self.horizontalLayout.addWidget(self.btnOK)
		spacerItem1 = QtWidgets.QSpacerItem(40, 20, QtWidgets.QSizePolicy.Expanding, QtWidgets.QSizePolicy.Minimum)
		self.horizontalLayout.addItem(spacerItem1)
		self.verticalLayout.addLayout(self.horizontalLayout)

		self.retranslateUi()
		QtCore.QMetaObject.connectSlotsByName(self._dialog)

		# Connects the OK button to the close method
		self.btnOK.clicked.connect(self._close)

	def _close(self):
		"""
		Closes this dialog, that is all
		"""
		self._dialog.close()

	def retranslateUi(self):
		"""
		Sets text! 3/4 auto generated!
		"""
		_translate = QtCore.QCoreApplication.translate
		self._dialog.setWindowTitle(_translate("Dialog", "Mensagem"))
		self.labelMsg.setText(_translate("Dialog", self._msg))
		self.btnOK.setText(_translate("Dialog", "OK"))

