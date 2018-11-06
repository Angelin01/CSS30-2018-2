from PyQt5 import QtCore, QtGui, QtWidgets
from items_list import FormType, ItemList


class Ui_MainWindow(object):
	def setupUi(self, MainWindow):
		"""
		Mostly auto generated stuff by pyuic5 from the .ui files
		Not auto-generated stuff is isolated by whitespace and has comments explaining changes
		"""
		MainWindow.setObjectName("MainWindow")
		MainWindow.resize(804, 523)
		MainWindow.setCursor(QtGui.QCursor(QtCore.Qt.ArrowCursor))
		self.centralwidget = QtWidgets.QWidget(MainWindow)
		self.centralwidget.setObjectName("centralwidget")
		self.horizontalLayoutWidget = QtWidgets.QWidget(self.centralwidget)
		self.horizontalLayoutWidget.setGeometry(QtCore.QRect(11, 14, 781, 221))
		self.horizontalLayoutWidget.setObjectName("horizontalLayoutWidget")
		self.MainLayout = QtWidgets.QHBoxLayout(self.horizontalLayoutWidget)
		self.MainLayout.setContentsMargins(0, 0, 0, 0)
		self.MainLayout.setObjectName("MainLayout")
		spacerItem = QtWidgets.QSpacerItem(40, 20, QtWidgets.QSizePolicy.Expanding, QtWidgets.QSizePolicy.Minimum)
		self.MainLayout.addItem(spacerItem)
		self.verticalLayout = QtWidgets.QVBoxLayout()
		self.verticalLayout.setObjectName("verticalLayout")
		self.btnGetPlaneTickets = QtWidgets.QPushButton(self.horizontalLayoutWidget)
		self.btnGetPlaneTickets.setMinimumSize(QtCore.QSize(200, 40))
		font = QtGui.QFont()
		font.setFamily("Calibri")
		font.setPointSize(12)
		self.btnGetPlaneTickets.setFont(font)
		self.btnGetPlaneTickets.setObjectName("btnGetPlaneTickets")
		self.verticalLayout.addWidget(self.btnGetPlaneTickets)
		self.btnGetLodgings = QtWidgets.QPushButton(self.horizontalLayoutWidget)
		self.btnGetLodgings.setMinimumSize(QtCore.QSize(200, 40))
		font = QtGui.QFont()
		font.setFamily("Calibri")
		font.setPointSize(12)
		font.setBold(False)
		font.setWeight(50)
		font.setKerning(False)
		self.btnGetLodgings.setFont(font)
		self.btnGetLodgings.setObjectName("btnGetLodgings")
		self.verticalLayout.addWidget(self.btnGetLodgings)
		self.btnGetTravelPackages = QtWidgets.QPushButton(self.horizontalLayoutWidget)
		self.btnGetTravelPackages.setMinimumSize(QtCore.QSize(200, 40))
		font = QtGui.QFont()
		font.setFamily("Calibri")
		font.setPointSize(12)
		self.btnGetTravelPackages.setFont(font)
		self.btnGetTravelPackages.setObjectName("btnGetTravelPackages")
		self.verticalLayout.addWidget(self.btnGetTravelPackages)
		self.MainLayout.addLayout(self.verticalLayout)
		spacerItem1 = QtWidgets.QSpacerItem(40, 20, QtWidgets.QSizePolicy.Expanding, QtWidgets.QSizePolicy.Minimum)
		self.MainLayout.addItem(spacerItem1)
		MainWindow.setCentralWidget(self.centralwidget)
		self.statusbar = QtWidgets.QStatusBar(MainWindow)
		self.statusbar.setObjectName("statusbar")
		MainWindow.setStatusBar(self.statusbar)

		self._retranslateUi(MainWindow)
		QtCore.QMetaObject.connectSlotsByName(MainWindow)
		MainWindow.setTabOrder(self.btnGetPlaneTickets, self.btnGetLodgings)

		# ======================================== #
		# Connects the buttons to methods
		# ======================================== #
		self.btnGetPlaneTickets.clicked.connect(self._showPlaneTickets)
		self.btnGetLodgings.clicked.connect(self._showLodgings)
		self.btnGetTravelPackages.clicked.connect(self._showTravelPackages)

	def _retranslateUi(self, MainWindow):
		"""
		Sets the text in the window
		"""
		_translate = QtCore.QCoreApplication.translate
		MainWindow.setWindowTitle(_translate("MainWindow", "Agência de Viagens"))
		self.btnGetPlaneTickets.setText(_translate("MainWindow", "Consultar Passagens"))
		self.btnGetLodgings.setText(_translate("MainWindow", "Consultar Hospedagens"))
		self.btnGetTravelPackages.setText(_translate("MainWindow", "Consultar Pacotes"))

	def _showItemList(self, formType: FormType):
		"""
		Shows a form with the desired type: FormType.PLANE_TICKET, FormType.LODGING or FormType.TRAVEL_PACKAGE
		:param formType: A FormType for the desired ItemList
		"""
		ui = ItemList(formType)
		ui.setupUi()
		self.itemListUi = ui  # Must keep the reference otherwise garbage collector will destroy the window
		ui.form.show()

	# Methods for connecting the buttons to.
	# Should be self explanatory
	def _showPlaneTickets(self):
		self._showItemList(FormType.PLANE_TICKET)

	def _showLodgings(self):
		self._showItemList(FormType.LODGING)

	def _showTravelPackages(self):
		self._showItemList(FormType.TRAVEL_PACKAGE)


if __name__ == "__main__":
	import sys
	app = QtWidgets.QApplication(sys.argv)
	MainWindow = QtWidgets.QMainWindow()
	ui = Ui_MainWindow()
	ui.setupUi(MainWindow)
	MainWindow.show()
	sys.exit(app.exec_())

