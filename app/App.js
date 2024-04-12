import { StatusBar } from 'expo-status-bar';
import { SafeAreaView, StyleSheet, Text, View } from 'react-native';
import { WebView } from 'react-native-webview';


export default function App() {
  return (
    <SafeAreaView style={{
      flex: 1
    }}>
      <WebView
      source={{ uri: 'http://localhost:3000/login' }}
    />
    </SafeAreaView>
  );
}