from PyQt5 import QtCore, QtGui, QtWidgets

class Msg(object):
	def __init__(self, msg):
		self._msg = msg
		self._dialog = QtWidgets.QDialog()

	def setupUi(self):
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

	def retranslateUi(self):
		_translate = QtCore.QCoreApplication.translate
		self._dialog.setWindowTitle(_translate("Dialog", "Mensagem"))
		self.labelMsg.setText(_translate("Dialog", self._msg))
		self.btnOK.setText(_translate("Dialog", "OK"))

