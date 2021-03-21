#define INPUT_COUNT       5
#define IN_STATE_IDLE     0
#define IN_STATE_CONTACT  1

unsigned int g_inputState[INPUT_COUNT];
unsigned int g_measurements[INPUT_COUNT];

void setup() {
  for(int i=0; i<INPUT_COUNT; i++){
    g_inputState[i] = 0;
    g_measurements[i] = 0; //this should be loaded from EEPROM on startup
  }
  
  Serial.begin(9600);
  pinMode(A7, INPUT);
  Serial.println("DCSduino ready");
}

void loop() {
  switch (g_inputState[0]){
    case IN_STATE_IDLE: {
      if (analogRead(A7) < 164){
        g_inputState[0] = IN_STATE_CONTACT;
        
        g_measurements[0]++;
        Serial.println("INPUT 0: tick");
        Serial.print("CURR. 0: ");
        Serial.println(g_measurements[0]);
      }
      break;
    }
    case IN_STATE_CONTACT: {
      if (analogRead(A7) > 164){
        g_inputState[0] = IN_STATE_IDLE;
      }
      break;
    }
  }
}
